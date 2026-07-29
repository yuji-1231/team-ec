package jp.co.internous.framepj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.framepj.model.domain.MstUser;
import jp.co.internous.framepj.model.mapper.MstUserMapper;
import jp.co.internous.framepj.model.session.LoginSession;

/**
 * マイページに関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/frameweb/mypage")
public class MyPageController {
	
	/*
	 * フィールド定義
	 */
	@Autowired
	private MstUserMapper mstUserMapper;
	@Autowired
	private LoginSession loginSession;
	
	/**
	 * マイページ画面を初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return マイページ画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		/*
		 * 「08_画面設計書_マイページ.pdf」を表示する。
			※DBから取得する内容は以下の【出力内容】を参照。
			※ログイン中ユーザーの情報を表示する。
			【出力内容】
			氏名,氏名ふりがな,性別,ユーザー名,パスワード
		 */
		MstUser user = mstUserMapper.findByUserNameAndPassword(
				loginSession.getUserName(),
				loginSession.getPassword());
		m.addAttribute("user", user);
		m.addAttribute("loginSession",loginSession);
		return "my_page";
		
	}
}
