import { snapshotConstructor } from "firebase-functions/lib/providers/firestore";
import { dbService, realtimeService } from "../db";
import router from "../router";

export const board = (req, res) =>
  res.render("board", { pageTitle: "게시판", boards });

export const getBoardUpload = (req, res) =>
  res.render("boardUpload", { pageTitle: "게시글 쓰기" });
export const postBoardUpload = async (req, res) => {
  const {
    body: { title, content },
  } = req;
  const board = realtimeService.ref("Board").child("BoardData");
  const postBoard = board.push();
  postBoard.set({
    date: Date(),
    board_No: postBoard.key,
    comment: "comment",
    content,
    get: 0,
    hit: 0,
    id: "rrr1234",
    title,
  });
  res.redirect(router.loginhome);
};

export const boardDetail = (req, res) =>
  res.render("boardDetail", { pageTitle: "게시글" }); // 이건 후에 게시글 제목이 보이도록.
export const editBoard = (req, res) =>
  res.render("editBoard", { pageTitle: "글 수정" });
