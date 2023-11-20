# Group: photos38

## Members
- Marcc Rizzolo (NetID: mtr120)
- Bhavya Patel (NetID: bsp75)

## Application Launch Configuration

launch.json
```markdown
This application uses the following launch configuration:

{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch App",
            "request": "launch",
            "mainClass": "Photos",
            "vmArgs": "--module-path \"path_to_javafx_sdk_on_your_system\" --add-modules javafx.controls,javafx.fxml"
        }
    ]
}
```

Please replace `path_to_javafx_sdk_on_your_system` with the actual path to the JavaFX SDK on your Windows system.

Once application is started, a default data.dat will be generated in `\data\` with a stock user and stock album. 

### Nonstandard Login credentials:
Admin Username: "admin"
Stock Username: "stock"

## Note for macOS Users

This application was developed on a Windows system. If you want to run it on macOS, you need to replace the dependencies under the `lib` folder with the macOS JavaFX `.jar` files.
```

