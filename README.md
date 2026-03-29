这是一个专为原版纯生存服务器制作的等级系统(并没有什么卵用)
支持：
| 项目                 | 支持情况                     |
| -------------------- | ------------------------    |
| **服务端**            | Leaf、Paper、Spigot、Bukkit |
| **游戏版本**          | 1.21.x                      |
| **Java版本**          | Java 21+                    |
| **PlaceholderAPI**    | 2.11.6 - 2.12.2            |
| **数据库**            | YAML（自带）、可扩展MySQL    |



PlaceholderAPI 变量:
| 变量                       | 说明            | 示例         |
| ------------------------ | ------------- | ---------- |
| `%levelsystem_level%`    | 当前级 (0-9)     | 5          |
| `%levelsystem_tier%`     | 当前段 (每10级=1段) | 2          |
| `%levelsystem_value%`    | 当前值 (0-99)    | 45         |
| `%levelsystem_total%`    | 总等级 (段×10+级)  | 25         |
| `%levelsystem_percent%`  | 升级进度百分比       | 45%        |
| `%levelsystem_progress%` | 进度条可视化        | ■■■■□□□□□□ |

