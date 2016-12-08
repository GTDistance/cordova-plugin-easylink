//
//  CDVEasyLink.m
//  HelloCordova
//
//  Created by Thomas.Wang on 2016/11/14.
//
//

#import "CDVEasyLink.h"

@implementation CDVEasyLink
-(void)pluginInitialize{
    m_easylink_config = [[EASYLINK alloc]initWithDelegate:self];
    
}

- (void)getWifiSSid:(CDVInvokedUrlCommand*)command
{
    NSString *ssidString = [EASYLINK ssidForConnectedNetwork];
    
    

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:ssidString];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}
-(void)startSearch:(CDVInvokedUrlCommand*)command
{
    NSString* ssidString = [command.arguments objectAtIndex:0];
    NSString* passwordString = [command.arguments objectAtIndex:1];
    
    
    NSMutableDictionary *wlanConfig = [NSMutableDictionary dictionaryWithCapacity:20];
    if( m_easylink_config == nil){
        m_easylink_config = [[EASYLINK alloc]initWithDelegate:self];
    }
    NSData *ssidData = [EASYLINK ssidDataForConnectedNetwork];
    
    [wlanConfig setObject:ssidData forKey:KEY_SSID];
    [wlanConfig setObject:passwordString forKey:KEY_PASSWORD];
    [wlanConfig setObject:[NSNumber numberWithBool:YES] forKey:KEY_DHCP];
    [m_easylink_config prepareEasyLink_withFTC:wlanConfig info:nil mode:EASYLINK_V2_PLUS];
    [m_easylink_config transmitSettings];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"开始搜索"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}
-(void)stopSearch:(CDVInvokedUrlCommand*)command
{
    //停止EasyLink，注意释放内存
    NSLog(@"stopTransmitting");
    [m_easylink_config stopTransmitting];
    [m_easylink_config unInit];
    m_easylink_config=nil;
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"停止搜索"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}

//注意：有些庆科模块的固件代码有回连机制，配置成功设备会返回数据给app，新版本设备配上网络以后是不回连的
#pragma mark - EasyLink delegate -
- (void)onFoundByFTC:(NSNumber *)ftcClientTag withConfiguration: (NSDictionary *)configDict
{
    NSLog(@"New device found!");
    [m_easylink_config configFTCClient:ftcClientTag
                     withConfiguration: [NSDictionary dictionary] ];
   
    
}
#pragma mark - EasyLink delegate -
- (void)onDisconnectFromFTC:(NSNumber *)ftcClientTag
{
    NSLog(@"Device disconnected!");
}

#pragma mark - UIAlertViewDelegate -
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    //停止EasyLink，注意释放内存
    NSLog(@"stopTransmitting");
    [m_easylink_config stopTransmitting];
    [m_easylink_config unInit];
    m_easylink_config=nil;
}


@end
