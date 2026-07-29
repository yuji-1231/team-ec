package jp.co.internous.framepj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.framepj.model.domain.TblCart;
import jp.co.internous.framepj.model.domain.dto.CartDto;
import jp.co.internous.framepj.model.form.CartForm;
import jp.co.internous.framepj.model.mapper.TblCartMapper;
import jp.co.internous.framepj.model.session.LoginSession;


/**
 * カート情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/frameweb/cart")
public class CartController {
	
	/*
	 * フィールド定義
	 */
	
	@Autowired
	private TblCartMapper tblCartMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();
	

	/**
	 * カート画面を初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		int userId;
		
		if(loginSession.isLoggedIn() == true) {
			userId = loginSession.getUserId();
		}else{
			userId = loginSession.getTmpUserId();
		}
		
		m.addAttribute("loginSession",loginSession);
		
		
		if(tblCartMapper.findCountByUserId(userId) > 0) {
			List<CartDto> carts = tblCartMapper.findByUserId(userId);
			m.addAttribute("carts",carts);
		}else {
	        m.addAttribute("carts", new ArrayList<CartDto>());
	    }
		
		return "cart";
	}


	/**
	 * カートに追加処理を行う
	 * @param f カート情報のForm
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/add")
	public String addCart(CartForm f, Model m) {
	    
	    TblCart cart = new TblCart();
	    
	    int userId;
	    if (loginSession.isLoggedIn() == true) {
	        userId = loginSession.getUserId();
	    } else {
	        userId = loginSession.getTmpUserId();
	    }
	    cart.setUserId(userId);
	    cart.setProductId(f.getProductId());
	    cart.setProductCount(f.getProductCount());
	    		
	    if(tblCartMapper.findCountByUserIdAndProductId(userId,f.getProductId()) > 0) {
	    	tblCartMapper.update(cart);
	    }else {
	    	tblCartMapper.insert(cart);
	    }
	    
	    return "redirect:/frameweb/cart/";
	}

	/**
	 * カート情報を削除する
	 * @param checkedIdList 選択したカート情報のIDリスト
	 * @return true:削除成功、false:削除失敗
	 */
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteCart(@RequestBody String checkedIdList) {
		List<Integer> checkedIds = gson.fromJson(checkedIdList, CartForm.class).getCheckedIdList();
		return tblCartMapper.deleteById(checkedIds) > 0;
	}
}
