# Выполнил: Мирошниченко Денис БПИ225

## Функциональность:

---

- Реализована система аутентификации и авторизация двух типов пользователей: посетителей и админов (с помощью передачи пароля через параметры HTTP request)

- Администратор может добавлять/удалять блюда, устанавливать различные характеристики (цена, кол-во, продолжительность приготовления в секунда)

- Посетители могут смотреть актуальное меню:

  *curl -X 'GET' 'http://localhost:8085/dish' -H 'accept: */*'*
    
  А далее совершать заказ: POST: http://localhost:8085/order

  Пример body:

  ```
    {
      "dishes": [
        {
          "title": "milk",
          "amount": 3
        },
        {
          "title": "bread",
          "amount": 5
        }
    
      ],
      "clientEmail": "admin",
      "password": "123"
    }
  ```
  *Блюда могут передаваться в качестве массива*

  Response: **OK! Order was created! with id = 363cf26c-e536-4aa4-9040-9c595249e926**

- Заказы обрабатываются в отдельных потоках за это ответственный сервис: **SheffService**. 

  **package restaurant.coredomain.application.services;**

- Посетители могут добавлять блюда в существующий заказ: 
  
  PUT: http://localhost:8085/order

  ```
  {
    "title": "title-of-dish",
    "amount": 0,
    "transactionId": "your-code(get when create order)",
    "email": "your-email",
    "password": "password"
  }
  ```
  
- Посетители могут отменять заказ

   DELETE: http://localhost:8085/order
   
  ```
  {
    "email": "your-email",
    "password": "password",
    "transactionId": "your-code(get when create order)"
  }
  ```

- Система отображает статус заказа:

  *Процесс подготовки:*

  ```
  {
    "started_at": "2024-03-07T20:53:30.778+00:00",
    "finished_at": "2024-03-07T20:54:15.776+00:00",
    "dishes": [
      {
        "title": "milk",
        "price": 77,
        "amount": 3
      },
      {
        "title": "bread",
        "price": 50,
        "amount": 5
      }
    ],
    "ready": false
  }
  ```

  *Статус готов:*

  ```
  {
    "started_at": "2024-03-07T20:53:30.778+00:00",
    "finished_at": "2024-03-07T20:54:15.776+00:00",
    "dishes": [
      {
        "title": "milk",
        "price": 77,
        "amount": 3
      },
      {
        "title": "bread",
        "price": 50,
        "amount": 5
      }
    ],
    "ready": true
  }
  ```
  
- По завершению работы пользователь может оплатить свой заказ

  POST: http://localhost:8085/order/pay/{transactionId}

  Responses: 
  
  - Order was paid! Good day
  - Order cant be paid (заказ уже был оплачен или он еще готовится)

- Присутствует сохранение состояние системы(напишу по endpoint'ам):

  - Получить все меню: GET: /dish - для всех пользователей
  - Получить всех пользователей системы: /admin/users/admin/123 - для админов
  - Получить суммы выручки ресторана: /admin/profit/admin/123 - для админов

## Критерии оценки:

- Применение принципов ООП и SOLID ```done```

- Аутентификация ```auth package```

- Использование шаблонов проектирования ```Abstract factory, Strategy```

- Хранение данных ```PostgreSQL```

- Реализация многопоточности для обработки заказов ```class SheffService```

- Codestyle ```done```

- readme с описанием того, как пользоваться программой, какие шаблоны были использованы в проекте и почему ```it's my readme -_-```

- Понятный интерфейс ```i hope you can lauch my app) All instractions are below```

## Как запустить проверяющему?

- Запустить Docker на своем компьютере.
- В корневой директории проекта написать ```docker-compose up```.
- Подождать пока  база данных проинициализируется.
- Запустить само веб приложение с помощью IDE.

Все критерии соблюдены, хотелось бы добавить, что в качестве базы данных использую PostgresSQL (init файл находится в ./utils относительно корневой папки проекта)
Использовались шаблоны проектирования: Абстрактная фабрика (для создания подключений к базе данных), Стратегия(для слабой связанности компонентов). Также есть такие паттерны как: Repository, UoW.
Для consistency базы данных в случае ошибок использую транзакции, которые мне помогает регулировать паттерн UoW.
Хотелось бы отметить, что я не использовал ORM для выполнения данного задания (надеюсь на Ваш респект ^_^ ).