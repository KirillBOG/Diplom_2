package dataForTesting;

import org.apache.commons.lang3.RandomUtils;
import users.User;
import users.UserEdited;

import java.util.Random;

public class GenerationData {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    public static User generateUser() {
        String email = generateRandomEmail();
        String password = generateRandomPassword(6, 10);
        String name = generateRandomName();
        return new User(email, password, name);
    }

    public static UserEdited generateUserEditedData() {
        String newEmail = generateRandomEmail();
        String newPassword = generateRandomPassword(6, 10);
        String newName = generateRandomName();
        return new UserEdited(newEmail, newPassword, newName);
    }

    public static int generateSizeForIngredientSublist(int max) {
        return RandomUtils.nextInt(1, max);
    }

    private static String generateRandomEmail() {
        String username = generateRandomString(5, 10);
        return username + "@yandex.ru";
    }

    private static String generateRandomPassword(int minLength, int maxLength) {
        int length = RandomUtils.nextInt(minLength, maxLength + 1);
        return generateRandomString(length);
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private static String generateRandomString(int minLength, int maxLength) {
        int length = RandomUtils.nextInt(minLength, maxLength + 1);
        return generateRandomString(length);
    }

    private static String generateRandomName() {

        String firstName = generateRandomString(3, 8);
        String lastName = generateRandomString(3, 8);
        return capitalize(firstName) + " " + capitalize(lastName);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
