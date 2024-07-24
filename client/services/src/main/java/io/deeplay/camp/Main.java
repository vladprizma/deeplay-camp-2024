package io.deeplay.camp;

import action.Action;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Action action = new Action();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите запрос:\n\t1.сделать ход\n\t2.запросить паузу");
            String string = scanner.nextLine();
            if (string.equals("1")) {
                System.out.println("Введите ваш ход");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                action.handleMoveAction(x, y);
            } else if (string.equals("2")) {
                action.handlePauseAction();
            } else {
                System.out.println("Такого параметра не существует. Попробуйте еще раз");
            }
        }
    }
}