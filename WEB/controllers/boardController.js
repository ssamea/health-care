import { dbService } from "../db";
import router from "../router";

export const board = (req, res) =>
  res.render("board", { pageTitle: "게시판", boards });

export const getBoardUpload = (req, res) =>
  res.render("boardUpload", { pageTitle: "게시글 쓰기" });
export const postBoardUpload = async (req, res) => {
  const {
    body: { title, description },
  } = req;
  const data = {
    title,
    description,
  };
  const board = dbService.collection("boards");
  const postBoard = board.doc().set(data);
  res.redirect(router.loginhome);
};

export const boardDetail = (req, res) =>
  res.render("boardDetail", { pageTitle: "게시글" }); // 이건 후에 게시글 제목이 보이도록.
export const editBoard = (req, res) =>
  res.render("editBoard", { pageTitle: "글 수정" });
