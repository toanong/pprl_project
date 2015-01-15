package rosita.linkage.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import cdc.components.AbstractJoin;
import cdc.configuration.ConfiguredSystem;
import cdc.gui.CancelThreadListener;
import cdc.gui.LinkageThread;
import cdc.gui.ThreadPriorityChangedListener;
import cdc.gui.components.progress.JoinDetailsPanel;
import cdc.gui.components.progress.JoinInfoPanel;
import cdc.gui.components.progress.ProgressDialog;
import cdc.gui.external.JXErrorDialog;
import cdc.utils.GuiUtils;
import cdc.utils.RJException;

public class LinkageProcessStarter implements ProcessStarterInterface {

	private rosita.linkage.tools.LinkageThread thread;
	
	public void startProcessAndWait(ConfiguredSystem system) {
		thread = new rosita.linkage.tools.LinkageThread(system);
		thread.start();
	}

}
