# Details

Date : 2024-03-09 17:53:39

Directory d:\\技术栈\\项目\\五子棋项目\\iGomokuGame\\iGomokuGame-Web

Total : 53 files,  35153 codes, 166 comments, 793 blanks, all 36112 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [.eslintrc.cjs](/.eslintrc.cjs) | JavaScript | 12 | 1 | 2 | 15 |
| [.prettierrc.json](/.prettierrc.json) | JSON | 8 | 0 | 0 | 8 |
| [index.html](/index.html) | HTML | 13 | 0 | 1 | 14 |
| [jsconfig.json](/jsconfig.json) | JSON with Comments | 8 | 0 | 1 | 9 |
| [package.json](/package.json) | JSON | 39 | 0 | 1 | 40 |
| [pnpm-lock.yaml](/pnpm-lock.yaml) | YAML | 2,208 | 0 | 324 | 2,532 |
| [src/App.vue](/src/App.vue) | vue | 15 | 0 | 1 | 16 |
| [src/api/account.js](/src/api/account.js) | JavaScript | 22 | 0 | 7 | 29 |
| [src/api/admin/admin.js](/src/api/admin/admin.js) | JavaScript | 4 | 0 | 2 | 6 |
| [src/api/admin/analysis.js](/src/api/admin/analysis.js) | JavaScript | 19 | 6 | 7 | 32 |
| [src/api/admin/management.js](/src/api/admin/management.js) | JavaScript | 15 | 2 | 5 | 22 |
| [src/api/friend.js](/src/api/friend.js) | JavaScript | 19 | 0 | 8 | 27 |
| [src/api/gameAI.js](/src/api/gameAI.js) | JavaScript | 10 | 4 | 5 | 19 |
| [src/assets/main.css](/src/assets/main.css) | CSS | 11 | 0 | 2 | 13 |
| [src/assets/map.json](/src/assets/map.json) | JSON | 27,115 | 0 | 1 | 27,116 |
| [src/components/ChatItem.vue](/src/components/ChatItem.vue) | vue | 62 | 3 | 11 | 76 |
| [src/components/CheckerBoard.vue](/src/components/CheckerBoard.vue) | vue | 27 | 0 | 3 | 30 |
| [src/components/FriendItem.vue](/src/components/FriendItem.vue) | vue | 103 | 2 | 15 | 120 |
| [src/components/ModifyInfoForm.vue](/src/components/ModifyInfoForm.vue) | vue | 83 | 0 | 13 | 96 |
| [src/components/ShowTab.vue](/src/components/ShowTab.vue) | vue | 36 | 0 | 4 | 40 |
| [src/components/UserInfoShow.vue](/src/components/UserInfoShow.vue) | vue | 162 | 5 | 20 | 187 |
| [src/components/bigscreen/RankingBoard.vue](/src/components/bigscreen/RankingBoard.vue) | vue | 68 | 0 | 7 | 75 |
| [src/components/bigscreen/TopHeader.vue](/src/components/bigscreen/TopHeader.vue) | vue | 47 | 0 | 7 | 54 |
| [src/components/bigscreen/UserAccessMap.vue](/src/components/bigscreen/UserAccessMap.vue) | vue | 131 | 1 | 4 | 136 |
| [src/components/bigscreen/UserAgePie.vue](/src/components/bigscreen/UserAgePie.vue) | vue | 95 | 1 | 7 | 103 |
| [src/components/bigscreen/UserGameLevelBar.vue](/src/components/bigscreen/UserGameLevelBar.vue) | vue | 77 | 1 | 7 | 85 |
| [src/main.js](/src/main.js) | JavaScript | 23 | 10 | 9 | 42 |
| [src/router/index.js](/src/router/index.js) | JavaScript | 137 | 14 | 8 | 159 |
| [src/stores/adminInfo.js](/src/stores/adminInfo.js) | JavaScript | 42 | 1 | 1 | 44 |
| [src/stores/bigscreen.js](/src/stores/bigscreen.js) | JavaScript | 30 | 4 | 4 | 38 |
| [src/stores/showingFriender.js](/src/stores/showingFriender.js) | JavaScript | 39 | 1 | 3 | 43 |
| [src/stores/userIder.js](/src/stores/userIder.js) | JavaScript | 102 | 1 | 4 | 107 |
| [src/utils/formatTime.js](/src/utils/formatTime.js) | JavaScript | 25 | 2 | 2 | 29 |
| [src/utils/request.js](/src/utils/request.js) | JavaScript | 59 | 9 | 6 | 74 |
| [src/utils/time.js](/src/utils/time.js) | JavaScript | 37 | 2 | 2 | 41 |
| [src/views/BoardAIWindow.vue](/src/views/BoardAIWindow.vue) | vue | 919 | 29 | 52 | 1,000 |
| [src/views/BoardFriendWindow.vue](/src/views/BoardFriendWindow.vue) | vue | 901 | 16 | 55 | 972 |
| [src/views/FeedbackWindow.vue](/src/views/FeedbackWindow.vue) | vue | 54 | 0 | 4 | 58 |
| [src/views/FriendWindow.vue](/src/views/FriendWindow.vue) | vue | 186 | 3 | 16 | 205 |
| [src/views/GameWindow.vue](/src/views/GameWindow.vue) | vue | 205 | 4 | 27 | 236 |
| [src/views/IntroductionWindow.vue](/src/views/IntroductionWindow.vue) | vue | 171 | 7 | 14 | 192 |
| [src/views/MainWindow.vue](/src/views/MainWindow.vue) | vue | 121 | 6 | 16 | 143 |
| [src/views/NotFound.vue](/src/views/NotFound.vue) | vue | 14 | 1 | 1 | 16 |
| [src/views/admin/AdminIndex.vue](/src/views/admin/AdminIndex.vue) | vue | 500 | 12 | 15 | 527 |
| [src/views/admin/AdminLogin.vue](/src/views/admin/AdminLogin.vue) | vue | 116 | 1 | 13 | 130 |
| [src/views/admin/BigScreen.vue](/src/views/admin/BigScreen.vue) | vue | 146 | 8 | 9 | 163 |
| [src/views/admin/LayOut.vue](/src/views/admin/LayOut.vue) | vue | 162 | 1 | 16 | 179 |
| [src/views/admin/WebsiteMonitor.vue](/src/views/admin/WebsiteMonitor.vue) | vue | 132 | 0 | 7 | 139 |
| [src/views/admin/analysis/GameAnalysis.vue](/src/views/admin/analysis/GameAnalysis.vue) | vue | 72 | 0 | 9 | 81 |
| [src/views/admin/analysis/WebsiteAnalysis.vue](/src/views/admin/analysis/WebsiteAnalysis.vue) | vue | 150 | 0 | 13 | 163 |
| [src/views/admin/management/FeedbackManagement.vue](/src/views/admin/management/FeedbackManagement.vue) | vue | 197 | 4 | 9 | 210 |
| [src/views/admin/management/UserManagement.vue](/src/views/admin/management/UserManagement.vue) | vue | 191 | 3 | 10 | 204 |
| [vite.config.js](/vite.config.js) | JavaScript | 13 | 1 | 3 | 17 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)