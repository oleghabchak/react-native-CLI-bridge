# React Native Bluetooth App Readme

## Introduction

This README provides an overview of a React Native app that utilizes two custom Bluetooth native modules written on Java and ObjectiveC, connected via a CLI bridge, and includes two video examples. This app demonstrates how to interact with Bluetooth devices using React Native and showcases the integration of native modules.

## Installation

# Follow these steps to set up and run the app:

1. Clone this repository to your local machine:
```
git clone https://github.com/oleghabchak/react-native-CLI-bridge.git
```

2. Navigate to the project directory:
```
cd react-native-CLI-bridge
```

3. Install the project dependencies:
```
yarn install

cd example && yarn install
```

### iOS

For iOS, run the following command:
```
cd ios && pod install

cd .. && npx react-native run-ios
```

### Android

For Android, run the following command:
```
react-native run-android
```

## Native Bluetooth Modules
SDM is a power library which allows programmer very easy and powerful tools to communicate with Bluetooth devices.
	SDM has rich and flexible API for communicating to device but also SDM provides fully automated process with handy built-in UI. This case user only has to implement basic interface for getting data from SDM and init main SDM object and that's all.
Test project only shows how to use this feature. Test project doesn't imlpement full set of API but it is very good explained in SDM API documentation.



## Examples

The app includes two video examples demonstrating its functionality:

1. **Bluetooth Device Scanning**: This video shows how the app scans on the **Android** platform and call method `showDeviceManager`.

https://github.com/oleghabchak/react-native-CLI-bridge/assets/71442227/c644f401-75c0-4510-8020-b2b00faf599d

2. **Bluetooth Device Scanning**: This video shows how the app scans on the **iOS** platform and call method `showDeviceManager`.

https://github.com/oleghabchak/react-native-CLI-bridge/assets/71442227/b311a174-d2da-4770-b985-effaa9625246


## Troubleshooting

If you encounter any issues or errors while setting up or running the app, please refer to the project's issue tracker on GitHub or seek assistance from the development team.

## Contributions

Contributions to this project are welcome! If you have any improvements or bug fixes to propose, please create a pull request on the GitHub repository.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Special thanks to the developers of the custom Bluetooth native modules used in this project.
- We would like to acknowledge the React Native community for their valuable contributions and support.
