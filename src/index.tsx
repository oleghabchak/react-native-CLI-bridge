import { NativeModules } from 'react-native';

const SioDexModule = NativeModules.SioDexModule;
const SDMModule = NativeModules.AwesomeModule2;

export function showDeviceManager() {
  return SDMModule.showDeviceManager();
}

export function DexDownload(address: string) {
  return SioDexModule.startDownloading(address, 0);
}

export const sdmmodule = SDMModule;
export const sioModule = SioDexModule;
