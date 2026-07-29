package jp.co.internous.framepj.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.framepj.model.domain.MstProduct;
import jp.co.internous.framepj.model.form.SearchForm;
import jp.co.internous.framepj.model.mapper.MstCategoryMapper;
import jp.co.internous.framepj.model.mapper.MstProductMapper;
import jp.co.internous.framepj.model.session.LoginSession;

/**
 * 商品検索に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/frameweb")
public class IndexController {
	
			
	
	@Autowired
	private MstProductMapper productMapper;
	
	@Autowired
	private MstCategoryMapper categoryMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	/**
	 * トップページを初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		
		if(loginSession.isLoggedIn() == false && loginSession.getTmpUserId() == 0) {
			Random random = new Random();
			int tmpUserId = -(random.nextInt(900000000) + 100000000);
			loginSession.setTmpUserId(tmpUserId);
		} 
		m.addAttribute("categories", categoryMapper.find());
		m.addAttribute("products", productMapper.find());
		m.addAttribute("selected", 0);
		m.addAttribute("keywords", "");
		m.addAttribute("loginSession",loginSession);
		return "index";
	}
	
	/**
	 * 検索処理を行う
	 * @param f 検索用フォーム
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/searchItem")
	public String searchItem(SearchForm f, Model m) {
		
		
		int category = f.getCategory();
		String keywords = f.getKeywords();
		keywords = keywords.replace("　", " ");
		keywords = keywords.replaceAll(" +",  " ");
		keywords = keywords.trim();
		
		String[] keywordArray = keywords.split(" ");
		
		List<MstProduct> products;
		
		if (category == 0 && keywords.isEmpty()) {
			products = productMapper.find();
		} else if (category == 0) {
			products = productMapper.findByProductName(keywordArray);
		} else {
			products = productMapper.findByCategoryAndProductName(category,  keywordArray);
		}
		m.addAttribute("products", products);
		m.addAttribute("categories", categoryMapper.find());
		m.addAttribute("selected", category);
		m.addAttribute("keywords", keywords);
		return "index";
		
	}
}
