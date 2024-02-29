package edu.school21.numbers;

public class NumberWorker {
    public boolean isPrime(int number) {
        if (number < 2) {
            throw new IllegalArgumentException("The number must be greater than or equal to 2");
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public int digitsSum(int number) {
        int sum = 0;
        int absNumber = Math.abs(number);

        while (absNumber > 0) {
            sum += absNumber % 10;
            absNumber /= 10;
        }

        return sum;
    }
}