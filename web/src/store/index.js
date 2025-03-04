import { createStore } from 'vuex';
import ModuleUser from "./user.js";
import MoudlePk from "./pk.js";

export default createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  getters: {
  },
  modules: {
    user: ModuleUser,
    pk: MoudlePk,
  },
});