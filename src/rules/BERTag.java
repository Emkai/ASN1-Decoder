package rules;

import java.util.ArrayList;

public class BERTag extends Tag {
	private String tagClass;
	private String tagCP;
	private int tagType;
	private int tagLength;
	private ArrayList<Byte> tagData;
	private ArrayList<Tag> tagTags;

	public BERTag(String tagClass, String tagCP, int tagType, int tagLength) {
		this.tagClass = tagClass;
		this.tagCP = tagCP;
		this.tagType = tagType;
		this.tagLength = tagLength;
	}

	@Override
	String getInfoAboutMe(){
		return "tagClass: " + tagClass + ", tagCP: " + tagCP + ", tagType: " + tagType + ", tagLength: " + tagLength;
	}

	public String getTagClass() {
		return tagClass;
	}

	public void setTagClass(String tagClass) {
		this.tagClass = tagClass;
	}

	public String getTagCP() {
		return tagCP;
	}

	public void setTagCP(String tagCP) {
		this.tagCP = tagCP;
	}

	public int getTagType() {
		return tagType;
	}

	public void setTagType(int tagType) {
		this.tagType = tagType;
	}

	public int getTagLength() {
		return tagLength;
	}

	public void setTagLength(int tagLength) {
		this.tagLength = tagLength;
	}

	public ArrayList<Byte> getTagData() {
		return tagData;
	}

	public void setTagData(ArrayList<Byte> dataBytes) {
		this.tagData = dataBytes;
	}
}
