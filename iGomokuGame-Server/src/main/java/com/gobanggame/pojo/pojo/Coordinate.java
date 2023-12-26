package com.gobanggame.pojo.pojo;

import lombok.Data;

@Data
public  class Coordinate {
        private int x;
        private int y;
        private int score; // 用于传递分数

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
            this.score = 0;
        }

        public Coordinate(int score, int x, int y) {
            this.score = score;
            this.x = x;
            this.y = y;
        }

    }