/**
 * 
 */
package com.sports.common.domain;

/**
 * @author Administrator7
 *
 */
public class SportField {
	
	//场地地址
	private String fieldLocation;
	
	//场地类型
	private String fieldType;
	
	//场地管理人电话
	private String adminPhone;
	
	//场地GPS信息
	private GPSLocation gpsLocation;
	
	//场地图片信息
	private String picsOfField;
	
	
	public SportField() {
		super();
	}

	public String getFieldLocation() {
		return fieldLocation;
	}

	public void setFieldLocation(String fieldLocation) {
		this.fieldLocation = fieldLocation;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getAdminPhone() {
		return adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	public GPSLocation getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(GPSLocation gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

	public String getPicsOfField() {
		return picsOfField;
	}

	public void setPicsOfField(String picsOfField) {
		this.picsOfField = picsOfField;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminPhone == null) ? 0 : adminPhone.hashCode());
		result = prime * result + ((fieldLocation == null) ? 0 : fieldLocation.hashCode());
		result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
		result = prime * result + ((gpsLocation == null) ? 0 : gpsLocation.hashCode());
		result = prime * result + ((picsOfField == null) ? 0 : picsOfField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SportField other = (SportField) obj;
		if (adminPhone == null) {
			if (other.adminPhone != null)
				return false;
		} else if (!adminPhone.equals(other.adminPhone))
			return false;
		if (fieldLocation == null) {
			if (other.fieldLocation != null)
				return false;
		} else if (!fieldLocation.equals(other.fieldLocation))
			return false;
		if (fieldType == null) {
			if (other.fieldType != null)
				return false;
		} else if (!fieldType.equals(other.fieldType))
			return false;
		if (gpsLocation == null) {
			if (other.gpsLocation != null)
				return false;
		} else if (!gpsLocation.equals(other.gpsLocation))
			return false;
		if (picsOfField == null) {
			if (other.picsOfField != null)
				return false;
		} else if (!picsOfField.equals(other.picsOfField))
			return false;
		return true;
	}
	
	
}
