import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:b21_printer/b21_printer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _info = 'Unknown';

  @override
  void initState() {
    super.initState();
    loadInfo();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> loadInfo() async {
    String info;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      info =
          await B21Printer.info ?? 'Unknown';
    } on PlatformException {
      info = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _info = info;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            ElevatedButton(onPressed: (){
              B21Printer.printPage();
            }, child: const Text("test print"),),
            Center(
              child: Text('device info:$_info\n'),
            ),
          ],
        ),
      ),
    );
  }
}
