package com.cabbage.biz.userInfo.view;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cabbage.biz.userInfo.user.UserService;
import com.cabbage.biz.userInfo.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MailController {
	
	private final JavaMailSenderImpl mailSender;
	@Qualifier("userService")
	private final UserService userService;

	//이메일 인증
	@PostMapping("/user/EmailAuth")
	@ResponseBody
	public int emailAuth(String userEmail) {
		
		
		//난수의 범위 111111 ~ 999999 (6자리 난수)
		Random random = new Random();
		int checkNum = random.nextInt(888888)+111111;
		
		//이메일 보낼 양식
		String setFrom = "1301eddie@naver.com"; //2단계 인증 x, 메일 설정에서 POP/IMAP 사용 설정에서 POP/SMTP 사용함으로 설정o
		String toMail = userEmail;
		String title = "회원가입 인증 이메일 입니다.";
		String content = "인증 코드는 " + checkNum + " 입니다." +
						 "<br>" +
						 "해당 인증 코드를 인증 코드 확인란에 기입하여 주세요.";
		
		try {
			MimeMessage message = mailSender.createMimeMessage(); //Spring에서 제공하는 mail API
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return checkNum;
	}
	
	//이메일 인증
		@PostMapping("/user/EmailAuth2")
		@ResponseBody
		public int emailAuth2(String userEmail, String userId) {
			
			
			
			UserVO user = userService.getUserByIdAndEmail(userEmail, userEmail);
			if(user == null)
				return -1;
			
			//난수의 범위 111111 ~ 999999 (6자리 난수)
			Random random = new Random();
			int checkNum = random.nextInt(888888)+111111;
			
			//이메일 보낼 양식
			String setFrom = "1301eddie@naver.com"; //2단계 인증 x, 메일 설정에서 POP/IMAP 사용 설정에서 POP/SMTP 사용함으로 설정o
			String toMail = userEmail;
			String title = "비밀번호 찾기 인증 이메일 입니다.";
			String content = "인증 코드는 " + checkNum + " 입니다." +
							 "<br>" +
							 "해당 인증 코드를 인증 코드 확인란에 기입하여 주세요.";
			
			try {
				MimeMessage message = mailSender.createMimeMessage(); //Spring에서 제공하는 mail API
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	            helper.setFrom(setFrom);
	            helper.setTo(toMail);
	            helper.setSubject(title);
	            helper.setText(content, true);
	            mailSender.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return checkNum;
		}

}
