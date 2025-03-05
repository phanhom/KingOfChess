import { createStore } from 'vuex';

export default {
    state: {
        status: 'matching', // matching, playing, matched
        socket: null,
        opponent_username: '',
        opponent_photo: '',
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateOpponentInfo(state, opinfo) {
            state.opponent_username = opinfo.username;
            state.opponent_photo = opinfo.photo;
        },
    },
    actions: {
    },
    getters: {
    },
    modules: {
    },
};