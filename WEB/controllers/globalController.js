import { dbService } from "../db";

export const home = (req, res) => res.render("home");

export const join = (req, res) => res.render("join", { pageTitle: "회원가입" });

export const login = (req, res) => res.render("login", { pageTitle: "로그인" });

export const logout = (req, res) => res.render("logout");

export const search = (req, res) => {
  const {
    query: { search_query: searchingBy },
  } = req;

  res.render("search", { searchingBy });
};

export const loginhome = async (req, res) => {
  console.log(dbService.collection("boards").get());
  let boards = [];
  const getBoards = async () => {
    const board = dbService.collection("boards");
    await board.get().then((snapshot) => {
      snapshot.forEach((doc) => {
        boards.push({
          title: doc.data().title,
          description: doc.data().description,
        });
      });
      console.log(boards);
    });
  };
  // console.log(boards);
  // console.log(dbService.collection("boards").get("default"));
  // console.log(board);
  // console.log(getBoards);-
  await getBoards();
  res.render("loginhome", { pageTitle: "로그인 후 화면", boards });
};
