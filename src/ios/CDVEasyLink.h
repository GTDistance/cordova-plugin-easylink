//
//  CDVEasyLink.h
//  HelloCordova
//
//  Created by Thomas.Wang on 2016/11/14.
//
//

#import <Cordova/CDV.h>
#import "EASYLINK.h"//加easylink头文件

@interface CDVEasyLink : CDVPlugin
{
    EASYLINK *m_easylink_config;
}
- (void)getWifiSSid:(CDVInvokedUrlCommand*)command;
- (void)startSearch:(CDVInvokedUrlCommand*)command;
- (void)stopSearch:(CDVInvokedUrlCommand*)command;
@end
