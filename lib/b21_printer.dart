
import 'dart:async';

import 'package:flutter/services.dart' ;

class B21Printer {
  static const MethodChannel _channel = MethodChannel('b21_printer');

  static Future<String?> get info async {

    // await _channel.invokeMethod('demo');
    final String? time = await _channel.invokeMethod('info');
    return time;
  }


  static printPage() async{
    var json = await rootBundle.loadString("format.json");
    await _channel.invokeMethod("print",{"pageInfo":json});
  }

}
