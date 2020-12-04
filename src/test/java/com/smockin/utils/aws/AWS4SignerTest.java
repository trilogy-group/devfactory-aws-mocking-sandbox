package com.smockin.utils.aws;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;

import com.smockin.admin.service.utils.aws.AWS4Signer;


public class AWS4SignerTest {
  final static String HEADER_1 = "header1";
  final static String HEADER_2 = "heaDEr2";
  final static String HEADER_3 = "header3";

  final static String ANY_VALUE = "anyValue";

  private Map<String, String> headers = new HashMap<>();

  @Before
  public void setUp() throws Exception {
    headers.clear();
  }

  @Test
  public void containsHeaderIsEmptTest() {
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));
  }

  @Test
  public void containsHeader1OnlyTest() {
    headers.put(HEADER_1, ANY_VALUE);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));
  }

  @Test
  public void containsHeaderBothKeysTest() {
    headers.put(HEADER_1, ANY_VALUE);
    headers.put(HEADER_2, ANY_VALUE);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));
  }

  @Test
  public void containsHeaderMoreThanCheckedTest() {
    headers.put(HEADER_1, ANY_VALUE);
    headers.put(HEADER_2, ANY_VALUE);
    headers.put(HEADER_3, ANY_VALUE);
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2));
  }

  @Test
  public void containsHeaderAnotherKeyTest() {
    headers.put(HEADER_3, ANY_VALUE);
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_2));
  }

  @Test
  public void containsHeaderCharCaseIsNotImportantTest() {
    headers.put(HEADER_2.toUpperCase(), ANY_VALUE);
    assertFalse(AWS4Signer.containsHeader(headers, HEADER_1));
    assertTrue(AWS4Signer.containsHeader(headers, HEADER_2.toLowerCase()));
  }

  @Test
  public void getHeaderEmptyTest() {
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");
  }

  @Test
  public void getHeaderHeader1OnlyTest() {
    headers.put(HEADER_1, ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");
  }

  @Test
  public void getHeaderBothHeadersTest() {
    headers.put(HEADER_1, ANY_VALUE);
    headers.put(HEADER_2, ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), ANY_VALUE);
  }

  @Test
  public void getHeaderMoreKeysThanNeededTest() {
    headers.put(HEADER_1, ANY_VALUE);
    headers.put(HEADER_2, ANY_VALUE);
    headers.put(HEADER_3, ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), ANY_VALUE);
  }

  @Test
  public void getHeaderOtherKeyOnlyTest() {
    headers.put(HEADER_3, ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), "");
  }

  @Test
  public void getHeaderCaseSensitivityTest() {
    headers.put(HEADER_3, ANY_VALUE);
    headers.put(HEADER_2.toUpperCase(), ANY_VALUE);
    assertEquals(AWS4Signer.getHeader(headers, HEADER_1), "");
    assertEquals(AWS4Signer.getHeader(headers, HEADER_2), ANY_VALUE);
  }
}
