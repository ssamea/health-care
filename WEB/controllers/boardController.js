import { boards } from "../db";

export const board = (req, res) =>
  res.render("board", { pageTitle: "게시판", boards });
export const boardUpload = (req, res) =>
  res.render("boardUpload", { pageTitle: "게시글 쓰기" });
export const boardDetail = (req, res) =>
  res.render("boardDetail", { pageTitle: "게시글" }); // 이건 후에 게시글 제목이 보이도록.
export const editBoard = (req, res) =>
  res.render("editBoard", { pageTitle: "글 수정" });
