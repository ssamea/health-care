import { dbService, authService, realtimeService } from "../db";
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
  const {
    body: { email, password, password2 },
  } = req;
  console.log(`${password}, ${password2}, ${email}`);
  if (password !== password2) {
    console.log("<script>alert('비밀번호가 다릅니다.') </script>");
    res.redirect(router.join);
  } else {
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

export const login = (req, res) => res.render("login", { pageTitle: "로그인" });

export const logout = (req, res) => res.render("logout");

export const search = (req, res) => {
  const {
    query: { search_query: searchingBy },
  } = req;

  res.render("search", { searchingBy });
};

export const loginhome = async (req, res) => {
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
  res.render("loginhome", { pageTitle: "로그인 후 화면", boards });
};
