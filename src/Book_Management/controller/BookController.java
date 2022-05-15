package Book_Management.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import Book_Management.model.BookBean;
import Book_Management.persistance.dao.BookDAO;
import Book_Management.persistance.dto.BookRequestDTO;
import Book_Management.persistance.dto.BookResponseDTO;

@Controller
public class BookController {
	@Autowired
	private BookDAO dao;
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String headerjsp()
	{
		return"header";
	}
	
	@RequestMapping(value="/displaybook", method=RequestMethod.GET)
	public String displayView(ModelMap model) {
		ArrayList<BookResponseDTO> list=dao.selectAll();
		model.addAttribute("list",list);
		return "displayBook";
	}
	
	@RequestMapping(value="/setupaddbook", method=RequestMethod.GET)
	public ModelAndView setupaddbook() {
		return new ModelAndView("addBook","bean",new BookBean());
	}
	
	@RequestMapping(value="/addbook", method=RequestMethod.POST)
	public String addbook(@ModelAttribute("bean") @Validated BookBean bean, BindingResult bs, ModelMap model) {
		if(bs.hasErrors()) {
			return "addBook";
		}
		BookRequestDTO dto=new BookRequestDTO();
		dto.setBookCode(bean.getBookCode());
		dto.setBookTitle(bean.getBookTitle());
		dto.setBookAuthor(bean.getBookAuthor());
		dto.setBookPrice(Double.valueOf(bean.getBookPrice()));
		int rs=dao.insertData(dto);
		if(rs==0) {
			model.addAttribute("error", "Insert Failed");
			return "addBook";
		}
		return "redirect:/displaybook";
	}
	
	@RequestMapping(value="/setupUpdateBook/{bookCode}", method=RequestMethod.GET)
	public ModelAndView setupUpdatebook(@PathVariable String bookCode) {
		BookRequestDTO dto=new BookRequestDTO();
		dto.setBookCode(bookCode);
		return new ModelAndView("updateBook","bean",dao.selectOne(dto));
	}
	
	@RequestMapping(value="/updatebook", method=RequestMethod.POST)
	public String updatebook(@ModelAttribute("bean") @Validated BookBean bean, BindingResult bs, ModelMap model) {
		if(bs.hasErrors()) {
			return "updateBook";
		}
		BookRequestDTO dto=new BookRequestDTO();
		dto.setBookCode(bean.getBookCode());
		dto.setBookTitle(bean.getBookTitle());
		dto.setBookAuthor(bean.getBookAuthor());
		dto.setBookPrice(Double.valueOf(bean.getBookPrice()));
		int rs=dao.updateData(dto);
		if(rs==0) {
			model.addAttribute("error", "Update Failed");
			return "updateBook";
		}
		return "redirect:/displaybook";
	}
	
	@RequestMapping(value="/deleteBook/{bookCode}", method=RequestMethod.GET)
	public String deleteBook(@PathVariable String bookCode,ModelMap model) {
		BookRequestDTO dto=new BookRequestDTO();
		dto.setBookCode(bookCode);
		int res=dao.deleteData(dto);
		if(res==0) {
			model.addAttribute("error", "Delete Failed");
		}
		return "redirect:/displaybook";
	}
}
