package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.constans.MessageContants;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;

@Controller
@RequestMapping(path = "/users")
public class UsersController {

	@Autowired
	private UserService uService;
	MessageContants message = new MessageContants();

	@GetMapping("")
	public String listAll(Model model) {
		List<Users> lstUsers = uService.listAllUser();

		model.addAttribute("lstUsers", lstUsers);

		return "users";
	}

	@GetMapping("/create")
	public String formCreateUser(Model model) {
		Users user = new Users();
		user.setEnabled(true);
		List<Role> lstRole = uService.listAllRole();
		model.addAttribute("lstRole", lstRole);
		model.addAttribute("user", user);
		return "users-form";
	}

	@PostMapping("/save")
	public String createUser(@ModelAttribute("user") Users user, RedirectAttributes redirectAttributes) {
		user.setPhotos("");
		uService.saveUser(user);
		redirectAttributes.addFlashAttribute("message", message.SUCCUSR00001);
		return "redirect:/users";

	}
}
