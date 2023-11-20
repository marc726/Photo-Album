# Group: photos38

## Members
- Marcc Rizzolo (NetID: mtr120)
- Bhavya Patel (NetID: bsp75)

```markdown
## Application Launch Configuration

This application uses the following launch configuration:

```jsonc
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

## Note for macOS Users

This application was developed on a Windows system. If you want to run it on macOS, you need to replace the dependencies under the `lib` folder with the macOS JavaFX `.jar` files.
```

