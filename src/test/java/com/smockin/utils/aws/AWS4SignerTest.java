package com.smockin.utils.aws;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;

import com.smockin.admin.service.utils.aws.AWS4Signer;


public class AWS4SignerTest {

  @Test
  public void containsHeaderTest() {
    final Map<String, String> headers = new HashMap<>();

    final String HEADER_1 = "header1";
    final String HEADER_2 = "heaDEr2";
    final String HEADER_3 = "header3";

    final String anyValue = "anyValue";

    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_1, anyValue);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_2, anyValue);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_3, anyValue);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.clear();
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_3, anyValue);
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));

    headers.put(HEADER_2.toUpperCase(), anyValue);
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2.toLowerCase()));
  }

  @Test
  public void getHeaderTest() {
    final Map<String, String> headers = new HashMap<>();

    final String HEADER_1 = "header1";
    final String HEADER_2 = "heaDEr2";
    final String HEADER_3 = "header3";

    final String anyValue = "anyValue";

    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_1, anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_2, anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);

    headers.put(HEADER_3, anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);

    headers.clear();
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_3, anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");

    headers.put(HEADER_2.toUpperCase(), anyValue);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), anyValue);
  }
}
