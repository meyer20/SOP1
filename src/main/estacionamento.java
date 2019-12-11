package main;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class estacionamento {
    static int parkLength = 20;
    static String[] parkingSpace = new String[parkLength];
    static Semaphore parkController = new Semaphore(1);;

    public static void main(String[] args) {
        new Thread(t).start();
        new Thread(t).start();
    }

    private static Runnable t = new Runnable() {
        @Override
        public void run() {
            try {
                entrance(parkController);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void entrance(Semaphore parkController) throws InterruptedException {
        while (true) {
            parkCar(parkingSpace, generateCarName(), parkController);
        }
    }

    public static boolean parkCar(String[] parkingSpace, String carName, Semaphore parkController) throws InterruptedException {
        boolean containsSpace = false;
        parkController.acquire();
        for (int i = 0; i < parkingSpace.length; i++) {
            if (parkingSpace[i] == null) {
                parkingSpace[i] = carName;
                containsSpace = true;
                System.out.println("Estacionei o carro " + carName + " na vaga " + (i + 1) + ".");
                parkController.release();
                return true;
//                break;
            }
        }

        if (!containsSpace) {
            System.out.println("Sem vagas disponiveís, liberando uma vaga!");
            Thread.sleep(1000);
            leaveRandomCar(parkLength, parkingSpace, parkController);
        }
        parkController.release();

        return containsSpace;
    }

    public static void leaveRandomCar(int parkLength, String[] parkingSpace, Semaphore parkController) throws InterruptedException {
        Random random = new Random();
        int spaceNumber = random.nextInt(parkLength);
        System.out.println(parkingSpace[spaceNumber] + " desocupou a vaga " + (spaceNumber + 1) + ".");
        parkingSpace[spaceNumber] = null;
    }

    public static String generateCarName() {
        Random random = new Random();
        return "Carro " + random.nextInt();
    }
}

