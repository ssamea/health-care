import { dbService, authService, realtimeService, User } from "../db";
import router from "../router";
import alert from "alert";
import { boardDetail } from "./boardController";

export const home = (req, res) => {
  res.render("home");
};

export const getjoin = (req, res) =>
  res.render("join", { pageTitle: "회원가입" });
export const postjoin = async (req, res) => {
  console.log(authService.error);
  let {
    body: {
      id,
      domain,
      password,
      password2,
      age,
      gender,
      height,
      weight,
      name,
    },
  } = req;
  if (password !== password2) {
    console.log("<script>alert('비밀번호가 다릅니다.') </script>");
    res.redirect(router.join);
  } else {
    const Client = realtimeService.ref("Client");
    const email = id + "@" + domain;
    const uploadClient = Client.child(id);
    uploadClient.set({
      age: (age *= 1),
      gender,
      height: (height *= 1),
      id,
      name,
      weight: (weight *= 1),
    });
    authService
      .createUserWithEmailAndPassword(email, password)
      .then((user) => {
        res.redirect(router.login);
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorCode);
        alert(errorMessage);
        res.redirect(router.join);
      });
  }
};

export const getlogin = (req, res) => {
  res.render("login", { pageTitle: "로그인" });
};
export const postlogin = (req, res) => {
  const {
    body: { email, password },
  } = req;
  authService
    .signInWithEmailAndPassword(email, password)
    .then((user) => {
      res.redirect(router.loginhome);
    })
    .catch((error) => {
      alert("아이디 또는 비밀번호가 잘못되었습니다.");
      res.redirect(router.login);
    });
};

export const logout = (req, res) => {
  authService
    .signOut()
    .then(() => {
      res.redirect(router.home);
    })
    .catch((error) => {
      alert("예상치못한 오류입니다. 새로고침 또는 브라우저 창을 닫아주세요.");
    });
};

export const search = (req, res) => {
  const {
    query: { search_query: searchingBy },
  } = req;

  res.render("search", { searchingBy });
};

export const loginhome = async (req, res) => {
  let Email;
  if (authService.currentUser !== null) {
    Email = authService.currentUser.email;
  }
  let boards = [];
  const getBoards = async () => {
    const board = realtimeService.ref("Board").child("BoardData");
    await board.once("value").then((snapshot) => {
      snapshot.forEach((childSnapshot) => {
        boards.push({
          title: childSnapshot.val().title,
          content: childSnapshot.val().content,
        });
      });
    });
  };
  await getBoards();
  res.render("loginhome", { pageTitle: "로그인 후 화면", boards, Email });
};
