public class Runner {
    public static void main(String[] args) {


        DbUserStorage dbUserStorage = new DbUserStorage();

        // Сохранение
//      dbUserStorage.save(new User(0, "Lora", "loraUserName", "123456", new Address(0, "lora_street"), new Telephone(0, "lora_telephone")));
//      dbUserStorage.save(new User(0, "Tony", "tony_smith", "123456", new Address(0, "tony_street"), new Telephone(0, "tony_telephone")));
//      dbUserStorage.save(new User(0, "Lora", "lora22_smith", "123456", new Address(0, "lora22_street"), new Telephone(0, "lora22_telephone")));

        // Получение данных по username
//        Optional<User> test = dbUserStorage.getByUsername("tony_smith");
//        System.out.println(test.get());

        // Получение данных по name
//        Optional<List> listOfUsers = dbUserStorage.getAllByName("Lora");
//        System.out.println(listOfUsers);

        // Получение всех данных
//        Optional<List> listOfUsers = dbUserStorage.getAll();
//        System.out.println(listOfUsers);

        // Получение данных по street
//        Optional<List> listOfUsers = dbUserStorage.getAllByStreet("lora_street");
//        System.out.println(listOfUsers);

        // Изменить имя по id
//        dbUserStorage.updateNameByID(1, "Mona");

        // Изменить пароль по id
//        dbUserStorage.updatePasswordByID(2, "qwerty");

        //Удалить запись из users по id
//        dbUserStorage.deleteById(3);

        // проверка существует ли юзер по name, username, password
//        boolean isUserExist = dbUserStorage.userExist(new User(2, "Tony", "tony_smith", "qwerty", new Address(0, "0"), new Telephone(0, "0")));
//        System.out.println("User exist: " + isUserExist);

        // проверка существует ли запись юзера по id
//        boolean isUserExist = dbUserStorage.userExistById(2);
//        System.out.println("User exist by Id: " + isUserExist);


    }
}
