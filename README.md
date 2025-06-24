# RecipeApp

一个基于 JavaFX 的食谱管理应用。

## 目录结构

SEII_Project/
│
├── pom.xml                        // Maven项目配置，依赖管理
├── cookbook.sql                   // MySQL数据库建表脚本
├── .gitignore                     // Git忽略配置
├── README.md                      // 项目说明文档
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/        // JavaFX控制器（业务逻辑与界面交互）
│       │   │   ├── RecipeLoginController.java
│       │   │   ├── RecipeManagementController.java
│       │   │   ├── RecipeInteractionController.java
│       │   │   └── ...
│       │   ├── dao/
│       │   │   └── mappers/      // MyBatis Mapper接口
│       │   │       ├── UsersMapper.java
│       │   │       ├── RecipesMapper.java
│       │   │       ├── IngredientsMapper.java
│       │   │       ├── CommentsMapper.java
│       │   │       └── RecipeIngredientsMapper.java
│       │   ├── model/            // 实体类（与数据库表对应）
│       │   │   ├── Users.java
│       │   │   ├── Recipes.java
│       │   │   ├── Ingredients.java
│       │   │   ├── Comments.java
│       │   │   └── RecipeIngredients.java
│       │   ├── view/             // JavaFX窗口/页面类
│       │   │   ├── LoginView.java
│       │   │   ├── SignupView.java
│       │   │   ├── RecipeListView.java
│       │   │   ├── RecipeCreateView.java
│       │   │   ├── RecipeDetailView.java
│       │   │   └── components/   // 可复用的界面组件
│       │   │       └── MessageAlert.java
│       │   └── Init/             // 应用入口
│       │       ├── App.java
│       │       └── AppEntrance.java
│       │
│       └── resources/
│           ├── mybatis_config.xml    // MyBatis主配置文件
│           ├── fxml/                // JavaFX界面描述文件
│           │   ├── login.fxml
│           │   ├── signup.fxml
│           │   ├── recipe_list.fxml
│           │   ├── recipe_edit_add.fxml
│           │   ├── recipe_detail.fxml
│           │   └── ...
│           ├── images/              // 图片资源
│           └── ...                  // 其他资源
│
└── target/                      // Maven编译输出目录（已被.gitignore忽略）

## 依赖
- Java 17+
- JavaFX
- Maven
