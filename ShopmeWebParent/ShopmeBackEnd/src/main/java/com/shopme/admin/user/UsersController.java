package com.shopme.admin.user;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.constans.MessageContants;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;
import com.shopme.common.exception.UserNotFoundException;

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
		model.addAttribute("pageTitle", message.TITLECREATE);
		return "users-form";
	}

	@PostMapping("/save")
	public String createUser(@ModelAttribute("user") Users user, RedirectAttributes redirectAttributes) {
		user.setPhotos("");
		uService.saveUser(user);
		redirectAttributes.addFlashAttribute("message", message.SUCCUSR00001);
		return "redirect:/users";

	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Users user = uService.findbyId(id);
			List<Role> lstRole = uService.listAllRole();
			model.addAttribute("lstRole", lstRole);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", MessageFormat.format(message.TITLEUPDATE, id));
			return "users-form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			uService.delete(id);
			redirectAttributes.addFlashAttribute("message", MessageFormat.format(message.SUCCUSR00002, id));;
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
	@GetMapping("/{id}/updateStatusEnable/{enabled}")
	public String updateStatusEnable(@PathVariable(name = "id") Integer id, @PathVariable(name = "enabled") boolean enabled,Model model, RedirectAttributes redirectAttributes) {
		try {
			uService.updateStatus(id,enabled);
			String status  =  enabled ? "enabled" : "disabled";
			redirectAttributes.addFlashAttribute("message", MessageFormat.format(message.SUCCUSR00003, id,status));;
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
}
