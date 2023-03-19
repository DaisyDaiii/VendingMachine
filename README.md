# T14B_GROUP1_ASM2

This project is the 2nd assignment of SOFT2412. Author is **T14B_GROUP1**.

## Installation 🔌
1. Clone the repository  
`git clone https://github.sydney.edu.au/SOFT2412-2020S2/T14B_GROUP1_ASM2.git`
2. Use *Gradle* to build & run the project  
`gradle clean build run`   
* To refresh the database of the vending machine please run  
`gradle test`

## Report generated for each role

The following are the paths for all the reports we generated.

#### Seller
**current available items**: `src/main/resources/database/Items`  
**sold items**: `src/main/resources/database/SoldItems`  

#### Cashier
**current available cash**: `src/main/resources/database/Cashies`  
**succeed transactions**: `src/main/resources/database/History`  

#### Owner
all the reports of seller's and cashier's and two more.     
**list of users**: `src/main/resources/database/Accounts`  
**lisr of cancelled transactions**: `src/main/resources/database/CancelledTransaction`

## Team Members 👾

| Name | Unikey |
|:---:|:---:|
| Chuyang Zhou | czho4938|
| Yuchen Shen | yshe6908 |
| Songyu Liang | slia3122 |
| Peixun Wu | pewu7151 |
| Ying Dai | ydai8556 |

### Role (Sprint 1)  
| Name | Role |
|:---:|:---:|
Peixun Wu | PO  
Yuchen Shen | SM  
Chuyang Zhou | Dev.  
Songyu Liang | Dev. 
Ying Dai | Dev.  
### Role (Start from Sprint 2)  
*Since this allocation did not work well in Sprint1, we changed the PO*  

| Name | Role | 
|:---:|:---:|   
Chuyang Zhou | PO   
Yuchen Shen | SM   
Songyu Liang | Dev.   
Ying Dai | Dev.   
Peixun Wu | Dev.   


## Development 🧩

### Tools
* Java      11.0.2
* Gradle    6.7
* Jenkins   2.264
* JetBrains IntelliJ IDEA
* GitHub & Git

### Development Rules

#### To start
**Never modify code in master branch directly!** Everytime you want to do development, you need to create a new branch with a *CamelCase* name clearly describe the function of the branch. Do your development in this new branch and open a pull request only after testing your code properly. While doing development, you need to create and manage corresponding project records in the *Projects pane*.
#### Code style
1. Writing code in *IntelliJ IDEA*, make sure **NO** error ❌ or warning ⚠️ is reported before trying to push your code.
2. Use annotation to write appropriate comment for each class and method. The comment should include:  
    * Description
    * `@param`
    * `@return`
#### Post issue on GitHub
There are several templates prepared, make sure you follow the template and write each section clearly.  
To post an issue, please follow:
1. Chose a template and answer each questions clearly.
2. The issue title should describe the main idea of the issue.
3. Make sure you choose correct **issue labels**.

### Test
We use *Gradle* to automatically run JUnit testcases.  
`gradle clean build test`

## Join us
**Please first read the development rule carefully, thank you !**  
We appreciate all contributions. If you are planning to contribute to our project, please follow the following steps:  
1. Open an issue and discuss your plan with us.
2. After getting our admission, please create a new branch to do the development. The branch name should follow **CamelCase**. For example, `DatabaseToolkit`.
3. After finishing coding, please first test your code by both writing JUnit testcases and running the app.
4. If all the tests looks good, please open a pull request and invite us to review.

## Contributing 👨‍👩‍👧‍👦
Unfortunately our project is private only, therefore unable to fork the project. You are welcomed to clone it and make pull request if you want to make our application better with new ideas or just help us fix some problems. If possible, please contact with us to discuss the details. Thank you.
