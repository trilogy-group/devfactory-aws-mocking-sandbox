package com.smockin.utils.aws;

import com.smockin.admin.service.utils.aws.AWS4Signer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class AWS4SignerTest {

  @Test
  public void containsHeaderTest() {
    final HashMap<String, String> headers = new HashMap<>();

    final String HEADER_1 = "header1";
    final String HEADER_2 = "heaDEr2";
    final String HEADER_3 = "header3";

    final String anyValue = "anyValue";

    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_1, anyValue);
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_2, anyValue);
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_3, anyValue);
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.clear();
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_3, anyValue);
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_2.toUpperCase(), anyValue);
    Assert.assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    Assert.assertTrue(AWS4Signer.containsHeader(headers, HEADER_2.toLowerCase()));
  }
  @Test
  public void getHeaderTest() {
    final HashMap<String, String> headers = new HashMap<>();

    final String HEADER_1 = "header1";
    final String HEADER_2 = "heaDEr2";
    final String HEADER_3 = "header3";

    final String anyValue = "anyValue";

    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_1, anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_2, anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);

    headers.put(HEADER_3, anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);

    headers.clear();
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_3, anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_2.toUpperCase(), anyValue);
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    Assert.assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);
  }

}
