export const users = (req, res) => res.render("users");
export const userDetail = (req, res) =>
  res.render("userDetail", { pageTitle: "개인 정보" });
export const editProfile = (req, res) =>
  res.render("editProfile", { pageTitle: "개인 정보 수정" });
export const changePassword = (req, res) =>
  res.render("changePassword", { pageTitle: "비밀번호 변경" });
