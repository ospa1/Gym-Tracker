package perscholas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import perscholas.database.dao.TutorialDAO;
import perscholas.database.entity.Tutorial;

@Controller
public class SlashController {

	@Autowired
	private TutorialDAO tutorialDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = { "/", "/index", "/index.html" })
	public ModelAndView slash() {
		ModelAndView response = new ModelAndView();
		response.setViewName("index");
		return response;
	}

	@RequestMapping(value = "/mainpage")
	public ModelAndView mainpage(HttpSession session) {

		ModelAndView result;

		if (session.getAttribute("email") != null) {
			result = new ModelAndView("/mainpage");
			String email = (String) session.getAttribute("email");
			result.addObject("email", email);
			result.addObject("message", "success!");
		} else {
			result = new ModelAndView("/login");
			result.addObject("message", "Need to login first");
		}

		return result;
	}

	@RequestMapping("/search")
	public ModelAndView search(@RequestParam(required = false) String search) {
		ModelAndView result = new ModelAndView("/search");

		List<Tutorial> videos = new ArrayList<>();

		if (!StringUtils.isEmpty(search)) {
			// find all videos
			videos = tutorialDao.findByNameIgnoreCase(search);
			result.addObject("search", search);
		}
		result.addObject("videos", videos);

		logger.debug(search);
		Integer videosSize = videos.size();
		logger.info("video count: " + videosSize.toString());
		return result;
	}

	@RequestMapping("/search/all")
	public ModelAndView searchAll(@RequestParam(required = false) String search) {
		ModelAndView result = new ModelAndView("/search");

		List<Tutorial> videos = tutorialDao.findAll();

		result.addObject("search", search);
		result.addObject("videos", videos);
		
		Integer videosSize = videos.size();
		logger.info("all video count: " + videosSize.toString());

		return result;
	}
}