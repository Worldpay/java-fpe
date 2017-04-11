# java-fpe
A Java implementation of Format Preserving Encryption using the FE1 scheme from the paper ["Format-Preserving Encryption" by Bellare, Rogaway, et al](http://eprint.iacr.org/2009/251).

Ported from [DotFPE](https://dotfpe.codeplex.com/); which was ported from [Botan Library](http://botan.randombit.net) Version 1.10.3.

See [LICENSE.md](https://github.com/Worldpay/java-fpe/blob/master/LICENSE.md) for the full license.

## Basic Usage

```java
/**
 * Sample code to round trip a number using FE1.
 * @throws FPEException If anything goes wrong.
 */
public void demoRoundTrip() throws FPEException {
	FE1 fe1 = new FE1();
	
	// The range of plaintext and ciphertext values 
	BigInteger modulus = new BigInteger("9999999999999999", 10);
	
	// A value to encrypt 
	BigInteger plaintextValue = new BigInteger("4444333322221111", 10);
	
	// A key, that will be used with the HMAC(SHA256) algorithm, note that this is not secure!
	byte[] hmacKey = new byte[] { 0x10, 0x20, 0x10, 0x20, 0x10, 0x20, 0x10, 0x20};
	
	// An initialisation vector, or tweak, used in the algorithm.
	byte[] iv = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
	
	BigInteger encryptedValue = fe1.encrypt(modulus, plaintextValue, hmacKey, iv);
	BigInteger decryptedValue = fe1.decrypt(modulus, encryptedValue, hmacKey, iv);
	
	System.out.println(String.format("Encrypted %s to %s and decrypted to %s", plaintextValue, encryptedValue,decryptedValue));
}
```

## How to build
You'll need Apache Ant and Apache Ivy to build.  Running `ant` in the project root will download, build, package and run all the unit tests.

Note that the ant script includes calls to Git to pull the commit hash and version number (tag) from the repo when building a new distribution.

## Dependencies

The main code (in [src](https://github.com/Worldpay/java-fpe/tree/master/src)) doesn't have any dependencies outside of the stanard Java APIs (including JCE extensions).

However, the unit test make us of the following:

1. __Logback__ provides the logging implementation.  
1. __slf4j__ provides the logging API.
1. __hamcrest-core-1.3__ and __Junit 4.11__ provide unit test support.

See [LICENSE.md](https://github.com/Worldpay/java-fpe/blob/master/LICENSE.md) for details of the associated licenses and restrictions associated with these dependencies.

## Copyright

Copyright Worldpay Ltd.