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
