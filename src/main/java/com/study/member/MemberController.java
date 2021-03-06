package com.study.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.study.utility.Utility;

@Controller // IOC ?
public class MemberController {

  @Autowired
  @Qualifier("com.study.member.MemberServiceImpl")
  private MemberService service;


  
  @GetMapping("/member/findPw")
  @ResponseBody
  public MemberDTO findPw(@RequestParam String mname, @RequestParam String email) {
    Map map = new HashMap();
    map.put("mname",mname);
    map.put("email",email);
    
    MemberDTO dto = service.findPw(map);
    
    return dto;
  }
  
  @GetMapping("/member/findPwForm")
  public String findpw() {
    return "/member/findPwForm";
  }
  
  @GetMapping("/member/findId")
  @ResponseBody
  public MemberDTO findId(@RequestParam String mname , @RequestParam String email) {
    Map map = new HashMap();
    map.put("mname",mname);
    map.put("email",email);
    
    MemberDTO dto = service.findId(map);
    
    return dto;
  }

  @GetMapping("/member/findIdForm")
  public String findid() {
    return "/member/findIdForm";
  }
  
  
  @GetMapping("/member/mypage")
  public String mypage(HttpSession session, Model model) {
     String id = (String)session.getAttribute("id");
   
    if(id==null) {
         return "redirect:/member/login/";
    }else {
    
         MemberDTO dto = service.mypage(id);
        
         model.addAttribute("dto", dto);
        
     return "/member/mypage";
    }
  }
  
  @RequestMapping("/admin/member/list")
  public String list(HttpServletRequest request) {
          // ????????????------------------------
          String col = Utility.checkNull(request.getParameter("col"));
          String word = Utility.checkNull(request.getParameter("word"));

          if (col.equals("total")) {
                  word = "";
          }

          // ???????????????-----------------------
          int nowPage = 1;// ?????? ???????????? ?????????
          if (request.getParameter("nowPage") != null) {
                  nowPage = Integer.parseInt(request.getParameter("nowPage"));
          }
          int recordPerPage = 3;// ??????????????? ????????? ???????????????

          int sno = (nowPage - 1) * recordPerPage;
          int eno = recordPerPage;

          Map map = new HashMap();
          map.put("col", col);
          map.put("word", word);
          map.put("sno", sno);
          map.put("eno", eno);

          int total = service.total(map);

          List<MemberDTO> list = service.list(map);

          String paging = Utility.paging(total, nowPage, recordPerPage, col, word);

          // request??? Model?????? ?????? ?????????
          request.setAttribute("list", list);
          request.setAttribute("nowPage", nowPage);
          request.setAttribute("col", col);
          request.setAttribute("word", word);
          request.setAttribute("paging", paging);
          
          return "/member/list";

  }
  
  @PostMapping("/member/update")
  public String update(MemberDTO dto, Model model) {
          int cnt = service.update(dto);
          
          if(cnt==1) {
                  model.addAttribute("id", dto.getId());
                  return "redirect:/";
          }else {
                  return "error";
          }
  }
  
  @GetMapping("/member/update")
  public String update(String id, HttpSession session, Model model) {
          
          if(id==null) {
                  id = (String)session.getAttribute("id");
          }
          
          MemberDTO dto = service.read(id);
          
          model.addAttribute("dto",dto);
          
          return "/member/update";
          
          
  }
  
  @PostMapping("/member/updateFile")
  public String updateFile(MultipartFile fnameMF, String oldfile, HttpSession session, HttpServletRequest request)
      throws IOException {
    String basePath = UploadMem.getUploadDir();

    if (oldfile != null && !oldfile.equals("member.jpg")) { // ???????????? ??????
      Utility.deleteFile(basePath, oldfile);
    }

    // storage??? ?????? ?????? ??????
    Map map = new HashMap();
    map.put("id", session.getAttribute("id"));
    map.put("fname", Utility.saveFileSpring(fnameMF, basePath));

    // ????????? ????????? ??????
    int cnt = service.updateFile(map);

    if (cnt == 1) {
      return "redirect:/member/mypage";       //?????????????????? ?????????????????? ?????? ??????!!
    } else {
      return "error";
    }
  }

  @GetMapping("/member/updateFileForm")
  public String updateFileForm() {
    
    return "/member/updateFile";
  }

  @GetMapping("/member/logout")
  public String logout(HttpSession session) {
    // session.removeAttribute("id");
    // session.removeAttribute("grade");
    session.invalidate();

    return "redirect:/";
  }

  @PostMapping("/member/login")
  public String login(@RequestParam Map<String, String> map, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, Model model) {
    int cnt = service.loginCheck(map);

    if (cnt > 0) {// ????????????.
      Map gmap = service.getGrade(map.get("id"));
      session.setAttribute("id", map.get("id"));
      session.setAttribute("grade", gmap.get("grade"));
      session.setAttribute("mname", gmap.get("mname"));

      // Cookie ??????,id?????? ?????? ??? id
      Cookie cookie = null;
      String c_id = request.getParameter("c_id");
      if (c_id != null) {
        cookie = new Cookie("c_id", c_id); // c_id=> Y
        cookie.setMaxAge(60 * 60 * 24 * 365);// 1???
        response.addCookie(cookie);// ?????????(client:???????????? ???) ?????? ??????

        cookie = new Cookie("c_id_val", map.get("id"));
        cookie.setMaxAge(60 * 60 * 24 * 365);// 1???
        response.addCookie(cookie);// ?????????(client:???????????? ???) ?????? ??????
      } else {
        cookie = new Cookie("c_id", ""); // ?????? ??????
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("c_id_val", "");// ?????? ??????
        cookie.setMaxAge(0);
        response.addCookie(cookie);

      }

    } // if cnt >0 end

    if (cnt > 0) {

      if (map.get("rurl") != null && !map.get("rurl").equals("")) {
        model.addAttribute("bbsno", map.get("bbsno"));
        model.addAttribute("nPage", map.get("nPage"));
        model.addAttribute("nowPage", map.get("nowPage"));
        model.addAttribute("col", map.get("col"));
        model.addAttribute("word", map.get("word"));

        return "redirect:" + map.get("rurl");
      } else {

        return "redirect:/";
      }

    } else {
      model.addAttribute("msg", "????????? ?????? ??????????????? ?????? ?????? ????????? <br>????????? ????????????. ???????????? ?????????");
      return "/member/errorMsg";
    }
  }

  @GetMapping("/member/login")
  public String login(HttpServletRequest request) {
    /*----???????????? ????????????----------------------------*/
    String c_id = ""; // ID ?????? ????????? ???????????? ??????, Y
    String c_id_val = ""; // ID ???

    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    if (cookies != null) {
      for (int i = 0; i < cookies.length; i++) {
        cookie = cookies[i];

        if (cookie.getName().equals("c_id")) {
          c_id = cookie.getValue(); // Y
        } else if (cookie.getName().equals("c_id_val")) {
          c_id_val = cookie.getValue(); // user1...
        }
      }
    }
    /*----???????????? ?????? ???----------------------------*/

    request.setAttribute("c_id", c_id);
    request.setAttribute("c_id_val", c_id_val);

    return "/member/login";
  }

  @PostMapping("/member/create")
  public String create(MemberDTO dto, HttpServletRequest request) throws IOException {
    String upDir = UploadMem.getUploadDir();
    String fname = Utility.saveFileSpring(dto.getFnameMF(), upDir);
    int size = (int) dto.getFnameMF().getSize();
    if (size > 0) {
      dto.setFname(fname);
    } else {
      dto.setFname("member.jpg");
    }

    if (service.create(dto) > 0) {
      return "redirect:/";
    } else {
      return "error";
    }
  }

  @GetMapping(value = "/member/emailcheck", produces = "application/json;charset=utf-8")
  @ResponseBody
  public Map<String, String> emailcheck(String email) {
    int cnt = service.duplicatedEmail(email);

    Map<String, String> map = new HashMap<String, String>();
    if (cnt > 0) {
      map.put("str", email + "??? ??????????????? ????????? ??? ????????????.");
    } else {
      map.put("str", email + "??? ????????????, ?????????????????????.");
    }
    return map;
  }

  @GetMapping(value = "/member/idcheck", produces = "application/json;charset=utf-8")
  @ResponseBody
  public Map<String, String> idcheck(String id) {
    int cnt = service.duplicatedId(id);

    Map<String, String> map = new HashMap<String, String>();
    if (cnt > 0) {
      map.put("str", id + "??? ??????????????? ????????? ??? ????????????.");
    } else {
      map.put("str", id + "??? ????????????, ?????????????????????.");
    }
    return map;
  }

  @GetMapping("/member/agree")
  public String agree() {

    return "/member/agree";
  }

  @PostMapping("/member/createForm")
  public String create() {
    return "/member/create";
  }

  @GetMapping("/")
  public String home() {

    return "/home";
  }

}
