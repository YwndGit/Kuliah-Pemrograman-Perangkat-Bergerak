import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Hello Flutter',
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Tugas 12 - PPB'),
          backgroundColor: Colors.blue,
          foregroundColor: Colors.white,
        ),
        body: const Center(
          child: Text(
            'Hello, Flutter!',
            style: TextStyle(
              fontSize: 28,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }
}