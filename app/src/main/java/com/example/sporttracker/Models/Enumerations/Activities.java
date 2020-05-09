package com.example.sporttracker.Models.Enumerations;

public enum Activities {

    RUN("Бег") {
        public float getCalories(float speed) {
            float x1;
            float y1;
            float x2;
            float y2;
            if (speed <= 8f) {
                x1 = 0;
                y1 = 0;
                x2 = 8;
                y2 = 6.9f;
            } else if (speed <= 10f) {
                x1 = 8;
                y1 = 6.9f;
                x2 = 10;
                y2 = 9;
            } else {
                x1 = 10;
                y1 = 9;
                x2 = 16;
                y2 = 10.7f;
            }
            return ((speed - x1) / (x2 - x1) * (y2 - y1) + y1) / 3600;
        }
    },
    WALK("Ходьба") {
        public float getCalories(float speed) {
            float x1;
            float y1;
            float x2;
            float y2;
            if (speed <= 4f) {
                x1 = 0;
                y1 = 0;
                x2 = 4;
                y2 = 2.6f;
            } else {
                x1 = 4;
                y1 = 2.6f;
                x2 = 6;
                y2 = 3.9f;
            }
            return ((speed - x1) / (x2 - x1) * (y2 - y1) + y1) / 3600;
        }
    },
    CYCLE("Езда на велосипеде") {
        public float getCalories(float speed) {
            float x1;
            float y1;
            float x2;
            float y2;
            if (speed <= 9f) {
                x1 = 0;
                y1 = 0;
                x2 = 9;
                y2 = 2.6f;
            } else if (speed <= 14f) {
                x1 = 9;
                y1 = 2.6f;
                x2 = 14;
                y2 = 4.3f;
            } else if (speed <= 15f) {
                x1 = 14;
                y1 = 4.3f;
                x2 = 15;
                y2 = 4.6f;
            } else {
                x1 = 15;
                y1 = 4.6f;
                x2 = 20;
                y2 = 7.7f;
            }
            return ((speed - x1) / (x2 - x1) * (y2 - y1) + y1) / 3600;
        }
    },
    SWIM("Плавание") {
        public float getCalories(float speed) {
            float x1;
            float y1;
            float x2;
            float y2;
            if (speed <= 0.4f) {
                x1 = 0;
                y1 = 0;
                x2 = 0.4f;
                y2 = 3f;
            } else {
                x1 = 0.4f;
                y1 = 3f;
                x2 = 2.4f;
                y2 = 6.6f;
            }
            return ((speed - x1) / (x2 - x1) * (y2 - y1) + y1) / 3600;
        }
    };

    private String name;

    Activities(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract float getCalories(float speed);

}
