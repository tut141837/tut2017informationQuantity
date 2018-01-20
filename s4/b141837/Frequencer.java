public class Frequencer {
  byte [] myTarget;
  byte [] mySpace;
  boolean targetReady = false;
  boolean spaceReady = false;

  int [] suffixArray;

  // 変数 "suffixArray"は、mySpaceのsuffixがソートされた配列です。
  // 各suffixは、mySpaceの開始位置である整数で表されます。
  // suffixArrayを出力するコードは次のとおりです。
  private void printSuffixArray() {
    if(spaceReady) {
      for(int i = 0; i < mySpace.length; i++) {
        int s = suffixArray[i];
        for(int j = s; j < mySpace.length; j++) {
          System.out.write(mySpace[j]);
        }
          System.out.write('\n');
      }
    }
  }

  private int suffixCompare(int i, int j) {
    // 2つのsuffixを辞書順で比較する。
    // i、jはsuffix_i、suffix_jを示す。

    // suffixArrayの作成に使用する。
    // <辞書順の例>
    // "i"    < "o"       : コードで比較する
    // "Hi"   < "Ho "     ; headが同じなら、次の要素を比較する
    // "Ho"   < "Ho "     ; suffixが同一であれば、長い文字列が大きい

    byte [] submySpace_i = java.util.Arrays.copyOfRange(mySpace, suffixArray[i], mySpace.length);
    byte [] submySpace_j = java.util.Arrays.copyOfRange(mySpace, suffixArray[j], mySpace.length);

    for(int a = 0; a < mySpace.length - i && a < mySpace.length - j; a++){
      if(submySpace_i[a] > submySpace_j[a]) return 1;
      else if(submySpace_i[a] < submySpace_j[a]) return -1;
    }

    if(i < j) return 1;
    if(i > j) return -1;
    return 0;

  }

  public void setSpace(byte []space) {
    mySpace = space;
    if(mySpace.length > 0) spaceReady = true;
    suffixArray = new int[space.length];


    for(int i = 0; i< space.length; i++) {
      suffixArray[i] = i;
    }


    //バブルソート
    int temp;
    for(int i = 0; i < space.length - 1; i++){
      for(int j = i + 1; j < space.length; j++){
        if(suffixCompare(i,j) == 1){
          temp = suffixArray[i];
          suffixArray[i] = suffixArray[j];
          suffixArray[j] = temp;
        }
      }
    }
    printSuffixArray(); // テスト用

    /*
    <"Hi Ho Hi Ho"の例>
    0: Hi Ho
    1: Ho
    2: Ho Hi Ho
    3:Hi Ho
    4:Hi Ho Hi Ho
    5:Ho
    6:Ho Hi Ho
    7:i Ho
    8:i Ho Hi Ho
    9:o
    A:o Hi Ho
    */
  }

  private int targetCompare(int i, int start, int end) {
    // suffix_iとtarget_start_endを辞書順で長さの制限付きで比較する。

    // suffixの適切なインデックスを検索するために使用する。
    // <検索の例>
    // suffix     target
    // "o"    >   "i"   // 1
    // "o"    <   "z"   // -1
    // "o"    =   "o"   // 0
    // "o"    <   "oo"  // -1
    // "Ho"   >   "Hi"  // 1
    // "Ho"   <   "Hz"  // -1
    // "Ho"   =   "Ho"  // 0
    // "Ho"   <   "Ho " // "Ho "はsuffix "Ho"のheadにない
    // "Ho"   =   "H"   // "H"はsuffix "Ho"のheadにあります

    byte [] submySpace_i = java.util.Arrays.copyOfRange(mySpace, suffixArray[i], mySpace.length);

    if (submySpace_i.length >= myTarget.length) {
      for (int a = start; a < end; a++) {
        if (submySpace_i[a] > myTarget[a]) return 1;
        else if (submySpace_i[a] < myTarget[a]) return -1;
      }
      return 0;
    }
    else return -1;
  }

  private int subByteStartIndex(int start, int end) {
    // subBytes以上の最初のsuffixのインデックスを返す。

    //例
    // For "Ho", it will return 5 for "Hi Ho Hi Ho".
    // For "Ho ", it will return 6 for "Hi Ho Hi Ho".

    for (int i = 0; i < mySpace.length; i++) {
      if (targetCompare(i, start, end) == 0) return i;
    }
    return suffixArray.length;
  }

  private int subByteEndIndex(int start, int end) {
    // subBytesより大きい最初のsuffixの次のインデックスを返す。

    // 例
    // For "Ho", it will return 7 for "Hi Ho Hi Ho".
    // For "Ho ", it will return 7 for "Hi Ho Hi Ho".

    for (int i = 0; i < mySpace.length-1; i++) {
      if (targetCompare(i, start, end) == 0 && targetCompare(i + 1, start, end) != 0) return i + 1;
    }
    /*for (int i = 0; i < mySpace.length; i++){
      if (targetCompare(i, start, end) == 1) return i;
    }*/
       return suffixArray.length;
  }

  public int subByteFrequency(int start, int end) {
    // この方法は遅いが、以下のように定義することができる。
    /*int spaceLength = mySpace.length;
    int count = 0;
    for(int offset = 0; offset< spaceLength - (end - start); offset++) {
      boolean abort = false;
      for(int i = 0; i< (end - start); i++) {
        if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
      }
      if(abort == false) { count++; }
    }*/

    int first = subByteStartIndex(start,end);
    int last1 = subByteEndIndex(start, end);

    // 検査コード
    /*for(int k=start;k<end;k++) { System.out.write(myTarget[k]); }
    system.out.printf(": first=%d last1=%d\n", first, last1);
    */
    return last1 - first;
  }

  public void setTarget(byte [] target) {
    myTarget = target;
    if(myTarget.length > 0) targetReady = true;
  }

  public int frequency() {
    if(targetReady == false) return -1;
    if(spaceReady == false) return 0;
    return subByteFrequency(0, myTarget.length);
  }
  public static void main(String[] args) { Frequencer frequencerObject;
    try {
      frequencerObject = new Frequencer();
      frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
      frequencerObject.setTarget("o Hi Ho".getBytes());
      int result = frequencerObject.frequency();
      System.out.print("Freq = " + result + " ");
      if(4 == result) { System.out.println("OK"); }
      else {System.out.println("WRONG"); }
    }
    catch(Exception e) {
      System.out.println("STOP");
    }
  }
}
