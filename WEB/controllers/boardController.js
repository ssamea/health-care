import { auth } from "firebase-admin";
import { snapshotConstructor } from "firebase-functions/lib/providers/firestore";
import { dbService, realtimeService, authService } from "../db";
import router from "../router";

export const board = (req, res) =>
  res.render("board", { pageTitle: "게시판", boards });

export const getBoardUpload = (req, res) => {
  res.render("boardUpload", { pageTitle: "게시글 쓰기" });
};
export const postBoardUpload = async (req, res) => {
  const {
    body: { title, content },
  } = req;
  let today = new Date();
  Date.prototype.format = function (f) {
    if (!this.valueOf()) return " ";

    var weekName = [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ];
    var d = this;
    var h;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function ($1) {
      switch ($1) {
        case "yyyy":
          return d.getFullYear();
        case "yy":
          return (d.getFullYear() % 1000).zf(2);
        case "MM":
          return (d.getMonth() + 1).zf(2);
        case "dd":
          return d.getDate().zf(2);
        case "E":
          return weekName[d.getDay()];
        case "HH":
          return d.getHours().zf(2);
        case "hh":
          return ((h = d.getHours() % 12) ? h : 12).zf(2);
        case "mm":
          return d.getMinutes().zf(2);
        case "ss":
          return d.getSeconds().zf(2);
        case "a/p":
          return d.getHours() < 12 ? "오전" : "오후";
        default:
          return $1;
      }
    });
  };

  String.prototype.string = function (len) {
    var s = "",
      i = 0;
    while (i++ < len) {
      s += this;
    }
    return s;
  };
  String.prototype.zf = function (len) {
    return "0".string(len - this.length) + this;
  };
  Number.prototype.zf = function (len) {
    return this.toString().zf(len);
  };
  const Email = authService.currentUser.email;
  let userId = Email.substring(0, Email.indexOf("@"));
  const board = realtimeService.ref("Board").child("BoardData");
  const postBoard = board.push();
  postBoard.set({
    Board_No: postBoard.key,
    content,
    Date: today.format("yyyy.MM.dd a/p hh:mm:ss"),
    Hits: 0,
    get: 0,
    id: userId,
    title,
  });
  res.redirect(router.loginhome);
};

export const boardDetail = (req, res) =>
  res.render("boardDetail", { pageTitle: "게시글" }); // 이건 후에 게시글 제목이 보이도록.
export const editBoard = (req, res) =>
  res.render("editBoard", { pageTitle: "글 수정" });
