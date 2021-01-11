module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: ["airbnb-base", "plugin:prettier/recommended"],
  parserOptions: {
    ecmaVersion: 12,
    sourceType: "module",
  },
  rules: {
    "no-console": "off",
    "spaced-comment": "off",
    "no-else-return": "off",
    "prettier/prettier": "off",
    "no-unused-vars": "off",
    "prefer-defult-export": "on",
  },
  parser: "babel-eslint",
};
