import * as firebase from "firebase/app";
import "firebase/database";
import "firebase/auth";
import admin from "firebase-admin";
import "firebase/firestore";
import serviceAccount from "./serviceKey.json";

const firebaseConfig = {
  apiKey: "AIzaSyBH3WFCIoX2SGHo-JfpGd7AS7BsrxTW8Dw",
  authDomain: "dailyhealth-6f85c.firebaseapp.com",
  databaseURL: "https://dailyhealth-6f85c-default-rtdb.firebaseio.com",
  projectId: "dailyhealth-6f85c",
  storageBucket: "dailyhealth-6f85c.appspot.com",
  messagingSenderId: "536591244877",
  appId: "1:536591244877:web:34c5d1f231dc9002804604",
  measurementId: "G-79PYPBLCTB",
};

firebase.initializeApp(firebaseConfig);

export const firebaseInstance = firebase;
export const realtimeService = firebase.database();
export const authService = firebase.auth();
export const dbService = firebase.firestore();
