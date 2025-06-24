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
│       │   │   └── ...
│       │   ├── dao/
│       │   │   └── ...      // MyBatis Mapper接口
│       │   ├── model/            // 实体类（与数据库表对应）
│       │   │   └── ...
│       │   ├── view/             // JavaFX窗口/页面类
│       │   │   └── ...
│       │   └── Init/             // 应用入口
│       │       └── ...
│       │
│       └── resources/
│           ├── mybatis_config.xml    // MyBatis主配置文件
│           ├── fxml/                // JavaFX界面描述文件
│           │   └── ...
│           ├── images/              // 图片资源
│           └── ...                  // 其他资源
│
└── target/                      // Maven编译输出目录（已被.gitignore忽略）

## 依赖
- Java 17+
- JavaFX
- Maven
