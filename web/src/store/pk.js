import { createStore } from 'vuex';

export default {
    state: {
        status: 'matching', // matching, playing, matched
        result: 'none', // p1, p2, deuce
        socket: null,
        opponent_username: '',
        opponent_photo: '',
        opponent_rating: '',
        game_map: null,
        game_obj: null,
        p1_id: null,
        p1_score: null,
        p1_x: null,
        p1_y: null,
        p1_direction: null,
        p2_id: null,
        p2_score: null,
        p2_x: null,
        p2_y: null,
        p2_direction: null,
        new_bullet: null,
        new_bullet_arrive: false,
        candy_x: null,
        candy_y: null,
        new_candy_arrive: false,
        total_time: null,
        time_used: null,
        online_count: null,
        chat_messages: [],
        new_message: '',
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
            state.opponent_rating = opinfo.rating;
        },
        updateGame(state, gamedata) {
            state.p1_id = gamedata.p1_id;
            state.p1_score = gamedata.p1_score;
            state.p1_x = gamedata.p1_x;
            state.p1_y = gamedata.p1_y;
            state.p1_direction = gamedata.p1_direction;
            state.p2_id = gamedata.p2_id;
            state.p2_score = gamedata.p2_score;
            state.p2_x = gamedata.p2_x;
            state.p2_y = gamedata.p2_y;
            state.p2_direction = gamedata.p2_direction;
        },
        updateGameMap(state, game_map) {
            state.game_map = game_map;
        },
        updateGameObj(state, game_obj) {
            state.game_obj = game_obj;
        },
        updateBullets(state, bullet) {
            state.new_bullet = bullet;
            // console.log(bullet.id)
            state.new_bullet_arrive = true;
        },
        updateResult(state, result) {
            state.result = result;
        },
        updateCandy(state, candy) {
            state.candy_x = candy.candy_x;
            state.candy_y = candy.candy_y;
            state.new_candy_arrive = true;
        },
        updateTime(state, time) {
            state.total_time = time.total,
                state.time_used = time.used;
        },
        updateOnlineCount(state, data) {
            state.online_count = data.online_count;
        },
        updateChatMessage(state, chat) {
            console.log(chat)
            state.new_message = chat;
            // state.chat_messages.push({
            //     user_id: chat.userid,
            //     message: chat.message,
            // });
        },
        send_message(state, msg) {
            if (state.socket != null) {
                state.socket.send(JSON.stringify({
                    event: 'message',
                    from_user_id: msg.from_id,
                    to_user_id: msg.to_id,
                    message: msg.msg,
                }));
            }
        },
    },
    actions: {
    },
    getters: {
    },
    modules: {
    },
};