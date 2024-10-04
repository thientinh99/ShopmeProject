package com.shopme.admin.user;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
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
		// List<Users> lstUsers = uService.listAllUser();

		// model.addAttribute("lstUsers", lstUsers);

		return listBypage(1, model);
	}

	@GetMapping("/pages/{pageNum}")
	public String listBypage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Users> page = uService.listByPage(pageNum);
		List<Users> lstUsers = page.getContent();
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		endCount = endCount > page.getTotalElements() ? page.getTotalElements() : endCount;
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
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
	public String createUser(@ModelAttribute("user") Users user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			Users saveuser = uService.saveUser(user);
			String uploadDir = "user-photos/" + saveuser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			user.setPhotos("");
			uService.saveUser(user);
		}
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
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			uService.delete(id);
			redirectAttributes.addFlashAttribute("message", MessageFormat.format(message.SUCCUSR00002, id));
			;
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/{id}/updateStatusEnable/{enabled}")
	public String updateStatusEnable(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "enabled") boolean enabled, Model model, RedirectAttributes redirectAttributes) {
		try {
			uService.updateStatus(id, enabled);
			String status = enabled ? "enabled" : "disabled";
			redirectAttributes.addFlashAttribute("message", MessageFormat.format(message.SUCCUSR00003, id, status));
			;
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
}
