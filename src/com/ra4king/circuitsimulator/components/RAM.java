package com.ra4king.circuitsimulator.components;

import com.ra4king.circuitsimulator.Component;
import com.ra4king.circuitsimulator.Simulator;
import com.ra4king.circuitsimulator.WireValue;
import com.ra4king.circuitsimulator.WireValue.State;

/**
 * @author Roi Atalla
 */
public class RAM extends Component {
	public static final int PORT_ADDRESS = 0;
	public static final int PORT_ENABLE = 1;
	public static final int PORT_CLK = 2;
	public static final int PORT_LOAD = 3;
	public static final int PORT_CLEAR = 4;
	public static final int PORT_DATA = 5;
	
	private WireValue[] memory;
	
	public RAM(Simulator simulator, String name, int bitSize, int addressBits) {
		super(simulator, "RAM " + name + "(" + bitSize + "," + addressBits + ")", new int[] {addressBits, 1, 1, 1, 1, bitSize });
		
		if(addressBits > 16 || addressBits <= 0) {
			throw new IllegalArgumentException("Address bits cannot be more than 16 bits.");
		}
		
		memory = new WireValue[1 << addressBits];
	}
	
	@Override
	public void valueChanged(WireValue value, int portIndex) {
		boolean enabled = ports[PORT_ENABLE].getWireValue().getBit(0) != State.ZERO;
		boolean load = ports[PORT_LOAD].getWireValue().getBit(0) != State.ZERO;
		boolean clear = ports[PORT_CLEAR].getWireValue().getBit(0) == State.ONE;
		
		WireValue address = ports[PORT_ADDRESS].getWireValue();
		
		switch(portIndex) {
			case PORT_ENABLE:
			case PORT_LOAD:
				if(!enabled || !load) {
					ports[PORT_DATA].pushValue(new WireValue(memory[0].getBitSize()));
				}
			case PORT_ADDRESS:
				if(enabled && load && address.isValidValue()) {
					ports[PORT_DATA].pushValue(memory[address.getValue()]);
				}
				break;
			case PORT_CLK:
				if(value.getBit(0) == State.ONE && address.isValidValue()) {
					memory[address.getValue()].set(ports[PORT_DATA].getWireValue());
				}
				break;
			case PORT_CLEAR:
				if(clear) {
					for(WireValue wireValue : memory) {
						wireValue.setAllBits(State.ZERO);
					}
				}
				break;
		}
	}
}