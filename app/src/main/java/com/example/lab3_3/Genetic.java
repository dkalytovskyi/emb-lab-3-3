package com.example.lab3_3;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
    private int a, b, c, d, y;

    Genetic(int A, int B, int C, int D, int Y) {
        a = A;
        b = B;
        c = C;
        d = D;
        y = Y;
    }

    public String calculate() {
        Random r = new Random();
        Double probability = 0.6;

        ArrayList<ArrayList<Integer>> population = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            population.add(new ArrayList<Integer>());

            for (int j = 0; j < 4; j++) {
                population.get(i).add(r.nextInt(y / 2) + 1);
            }
        }

        for (int it = 0; it < 0.5 * Integer.MAX_VALUE; it++) {
            Integer[] values = new Integer[4];
            Double percent = 0.0;

            for (int i = 0; i < population.size(); i++) {
                values[i] = getY(population.get(i));
                int delta = values[i] - y;
                if (delta == 0) {
                    return population.get(i).get(0) + " " +
                            population.get(i).get(1) + " " +
                            population.get(i).get(2) + " " +
                            population.get(i).get(3);
                }

                percent = percent + 1 / values[i];
            }


            // generating the wheel
            Double[] range = new Double[5];
            range[0] = 0.0;
            range[4] = 100.0;
            for (int i = 0; i < range.length - 1; i++) {
                range[i + 1] = range[i] + (values[i] / percent);

            }

            //choosing parents for the next gen

            Integer[] parentId = new Integer[4];

            for (int i = 0; i < parentId.length; i++) {
                int temp = r.nextInt(100);
                int id = 1;
                while (id < range.length && range[id] < temp) {
                    id++;
                }
                parentId[i] = (id == 0) ? id : id - 1;
            }
            Integer[][] children = new Integer[4][4];
            int x = -1;
            for (int i = 0; i < population.size(); i++) {
                Integer p1 = parentId[r.nextInt(parentId.length - 1)];
                Integer p2 = parentId[r.nextInt(parentId.length - 1)];
                int threshold = r.nextInt(3) + 1;
                for (int j = 0; j < children.length; j++) {
                    children[i][j] = (j < threshold) ? population.get(p1).get(j) : population.get(p2).get(j);
                }

                if (r.nextDouble() < probability) {
                    int choice = r.nextInt(children.length);
                    children[i][choice] += (r.nextDouble() > 0.5 && children[i][choice] < y / 2) ? 1 : (children[i][choice] > 1) ? -1 : 0;
                }

            }

            //   population = new Integer[children.length][children.length];

            population = new ArrayList<>(4);
            for (int i = 0; i < children.length; i++) {
                population.add(new ArrayList<Integer>());

                for (int j = 0; j < children.length; j++) {
                    population.get(i).add(children[i][j]);

                }
            }
        }
        int min = Integer.MAX_VALUE;
        int i = -1;
        for (int j = 0; j < population.size(); j++) {
            int tmp2 = getY(population.get(j));
            if (tmp2 - y < min) {
                min = tmp2;
                i = j;
            }
        }
        return "Could not find the answer, the closest I could get:\n" +
                population.get(i).get(0) + " " + population.get(i).get(1) + " " +
                population.get(i).get(2) + " " + population.get(i).get(3);
    }


    int getY(ArrayList<Integer> i) {
        return a * i.get(0) + b * i.get(1) + c * i.get(2) + d * i.get(3);
    }
}
